package arxiv

import java.net.URL

import arxiv.api.FeedMeta
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, ParallelTestExecution}

import scala.xml.XML

class FeedSpec extends FlatSpec with BeforeAndAfterAll with Matchers with ParallelTestExecution {

  val xmlString: String =
    """<?xml version="1.0" encoding="UTF-8"?>
      |<feed xmlns="http://www.w3.org/2005/Atom">
      |  <link href="http://arxiv.org/api/query?search_query%3Dall%3Aequation%20ANDNOT%2Ball%3Aprogram%26id_list%3D%26start%3D0%26max_results%3D10" rel="self" type="application/atom+xml"/>
      |  <title type="html">ArXiv Query: search_query=all:equation ANDNOT+all:program&amp;id_list=&amp;start=0&amp;max_results=10</title>
      |  <id>http://arxiv.org/api/tTt6/OtlWsU1mVsg6aI96gejTT4</id>
      |  <updated>2017-01-07T00:00:00-05:00</updated>
      |  <opensearch:totalResults xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">141956</opensearch:totalResults>
      |  <opensearch:startIndex xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">0</opensearch:startIndex>
      |  <opensearch:itemsPerPage xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">10</opensearch:itemsPerPage>
      |</feed>
    """.stripMargin

  "feed info" should "be correct" in {
    val xml = XML.loadString(xmlString)
    val feed = FeedMeta(xml)
    feed.id should be(new URL("http://arxiv.org/api/tTt6/OtlWsU1mVsg6aI96gejTT4"))
    feed.link should be(new URL("http://arxiv.org/api/query?search_query%3Dall%3Aequation%20ANDNOT%2Ball%3Aprogram%26id_list%3D%26start%3D0%26max_results%3D10"))
    feed.title should be("ArXiv Query: search_query=all:equation ANDNOT+all:program&id_list=&start=0&max_results=10")
    feed.updated.getTime should be(1483765200000L)
  }

}
