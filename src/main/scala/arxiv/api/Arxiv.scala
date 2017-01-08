package arxiv.api

import scala.xml.{Elem, XML}
import scalaj.http.{Http, HttpConstants}

object Arxiv {

  val sortOpt = SortOpt(SortBy.relevance, SortOrder.ascending)

  def request(qInterfaces: Seq[QInterface], sortOpt: SortOpt = sortOpt,
              connTimeoutMs: Int = defaultConn, readTimeoutMs: Int = defaultRead): Elem = {
    val params = qInterfaces.map(q => (q.key, q.toString)) ++ sortOpt.toParams
    val response = Http(apiURL).charset(HttpConstants.utf8).params(params).timeout(connTimeoutMs, readTimeoutMs)
    val responseString = response.asString
    if (responseString.isError) {
      throw new RuntimeException("error")
    }
    val body = responseString.body
    XML.loadString(body)
  }

}
