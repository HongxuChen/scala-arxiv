package arxiv.example

import java.nio.file.{Files, Paths}

import arxiv.api.Arxiv._
import arxiv.api.{Entry, FeedMeta, MaxResParam, PDFLink, QField, QueryOp, QueryParam, StartParam}
import com.typesafe.scalalogging.LazyLogging

import scalaj.http.{Http, HttpConstants}

object GetPDFLink extends LazyLogging {

  def saveFile(url: String, fileName: String): Unit = {
    logger.info(s"save ${fileName} from ${url}")
    val response = Http(url).charset(HttpConstants.utf8).timeout(connTimeoutMs = 4000, readTimeoutMs = 5000)
    val responseBytes = response.asBytes
    if (responseBytes.isError) {
      throw new RuntimeException("error")
    }
    val body = responseBytes.body
    Files.write(Paths.get(fileName), body)
  }

  def main(args: Array[String]): Unit = {
    val params = Seq(
      QueryParam(QField("all", "equation"), QField(QueryOp.ANDNOT, "all", "program")),
      StartParam(0),
      MaxResParam(10)
    )
    val xml = request(params)
    val feed = FeedMeta(xml)
    logger.info(s"${feed}")
    val entries = (xml \ "entry").map(Entry(_))
    for {
      entry <- entries
      link <- entry.links
    } {
      link match {
        case PDFLink(url) => {
          val realUrl = url.toString.replace("http", "https") + ".pdf"
          val id = entry.getAID.substring(1).replace('/', '_')
          val fileName = id + " - " + entry.title + ".pdf"
          logger.info(s"${realUrl}: ${fileName}")
          //          saveFile(realUrl, fileName)
        }
        case _ =>
      }
    }

  }

}
