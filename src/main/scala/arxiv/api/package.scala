package arxiv


package object api {

  val apiURL = "http://export.arxiv.org/api/query"

  val defaultStart = 0
  val defaultMaxRes = 10

  val defaultConn = 4000
  val defaultRead = 5000

  type ParamTy = Seq[(String, String)]

  implicit class ArxivStringOps(s: String) {
    def onliner: String = s.replaceAll("\\s+", " ")
  }


}
