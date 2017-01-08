package arxiv.api

import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.scalalogging.LazyLogging

import scala.xml.Node

case class AuthorInfo(authorName: String, affiliation: Option[String])

case class Entry(id: URL, title: String,
                 published: Date, updated: Date,
                 summary: String, authors: List[AuthorInfo],
                 links: List[Link],
                 categories: List[Category]) extends LazyLogging {
  val BASE = "http://arxiv.org/abs"

  def getAID: String = id.toString.drop(BASE.length)

  override def toString: String = {
    s"${id}: ${title}; ${categories.mkString("[", ", ", "]")}\n${links}"
  }

}

///////////////////////////////////////////////////////////
sealed trait Link {
  def url: URL
}

case class PDFLink(url: URL) extends Link

case class DOILink(url: URL) extends Link

case class AlterLink(url: URL) extends Link

///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

sealed trait Category {
  def s: String

  def isPrimary: Boolean = this match {
    case ArxivCat(_, isPrimary) => isPrimary
    case MSCat(_) => false
  }
}

object Category {
  def gen(s: String)(primary: String): List[Category] = {
    s.split(";|,").map { e =>
      val trimmed = e.trim
      if (trimmed.head.isDigit) MSCat(trimmed)
      else {
        val isPrimary = primary == trimmed
        ArxivCat(trimmed, isPrimary)
      }
    }.toList
  }
}

case class ArxivCat(s: String, primary: Boolean) extends Category

case class MSCat(s: String) extends Category

///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

object Entry {
  val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

  def apply(node: Node): Entry = {
    // guaranteed to be uniq
    val id = new URL((node \ "id").head.text)
    // first time
    val published = {
      val publishedString = (node \ "published").head.text
      sdf.parse(publishedString)
    }
    // last time
    val updated = {
      val updateString = (node \ "updated").head.text
      sdf.parse(updateString)
    }
    // unique
    val title = (node \ "title").head.text.onliner
    // unique
    val summary = (node \ "summary").head.text.onliner
    // authors: at least 1
    val authors: List[AuthorInfo] = {
      val subNodes = node \ "author"
      require(subNodes.length >= 1)
      val authorList: List[AuthorInfo] = subNodes.map { n =>
        val name = (n \ "name").head.text
        // TODO confirm there is only 1 affiliation
        val affiliation = (n \ "affiliation").headOption.map(_.text)
        AuthorInfo(name, affiliation)
      }(collection.breakOut)
      authorList
    }
    // links
    val links: List[Link] = {
      val linkSeq = for {
        link <- node \ "link"
      } yield {
        val linkTitle = (link \ "@title").headOption.map(_.text)
        val linkRel = (link \ "@rel").headOption.map(_.text)
        val linkHref = new URL((link \ "@href").head.text)
        (linkTitle, linkRel) match {
          case (Some(t), _) => {
            if (t == "pdf") PDFLink(linkHref)
            else if (t == "doi") DOILink(linkHref)
            else throw new RuntimeException("unknown title")
          }
          case (None, Some("alternate")) => {
            val linkType = (link \ "@type").head.text
            require(linkType == "text/html")
            AlterLink(linkHref)
          }
          case _ => throw new RuntimeException("unknown rel")
        }
      }
      linkSeq.toList
    }
    // categories: at least 1
    val categories = {
      val primaryLabel = {
        // TODO add ns: http://arxiv.org/schemas/atom
        val cat = (node \ "primary_category").head
        (cat \ "@term").head.text
      }
      val subNodes = node \ "category"
      val catText = for {
        cat <- subNodes
        term <- cat \ "@term"
        c <- Category.gen(term.text)(primaryLabel)
      } yield c
      require(catText.nonEmpty)
      catText.toList
    }
    Entry(id, title, published, updated, summary, authors, links, categories)

  }

}