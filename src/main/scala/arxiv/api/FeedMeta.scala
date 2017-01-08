package arxiv.api

import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

import scala.xml.Elem

case class FeedMeta(title: String, id: URL, link: URL, updated: Date)

object FeedMeta {
  val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")

  def apply(elem: Elem): FeedMeta = {
    val title = (elem \ "title").text
    val id = new URL((elem \ "id").text)
    val link = {
      val link = (elem \ "link").head
      val s = (link \ "@href").head.text
      new URL(s)
    }
    val updated = {
      val updatedString = (elem \ "updated").head.text
      sdf.parse(updatedString)
    }
    FeedMeta(title, id, link, updated)
  }
}