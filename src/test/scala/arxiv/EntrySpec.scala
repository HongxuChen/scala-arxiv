package arxiv

import java.net.URL

import arxiv.api.{ArxivCat, Entry}
import org.scalatest.{FlatSpec, Matchers}

import scala.xml.XML

class EntrySpec extends FlatSpec with Matchers {

  val entryString: String =
    """  <entry>
      |    <id>http://arxiv.org/abs/1212.2319v1</id>
      |    <updated>2012-12-11T07:33:36Z</updated>
      |    <published>2012-12-11T07:33:36Z</published>
      |    <title>Conservation laws of some lattice equations</title>
      |    <summary>  We derive infinitely many conservation laws for some multi-dimensionally
      |consistent lattice equations from their Lax pairs. These lattice equations are
      |the Nijhoff-Quispel-Capel equation, lattice Boussinesq equation, lattice
      |nonlinear Schr\"{o}dinger equation, modified lattice Boussinesq equation,
      |Hietarinta's Boussinesq-type equations, Schwarzian lattice Boussinesq equation
      |and Toda-modified lattice Boussinesq equation.
      |</summary>
      |    <author>
      |      <name>Jun-wei Cheng</name>
      |    </author>
      |    <author>
      |      <name>Da-jun Zhang</name>
      |    </author>
      |    <arxiv:doi xmlns:arxiv="http://arxiv.org/schemas/atom">10.1007/s11464-013-0304-z</arxiv:doi>
      |    <link title="doi" href="http://dx.doi.org/10.1007/s11464-013-0304-z" rel="related"/>
      |    <arxiv:journal_ref xmlns:arxiv="http://arxiv.org/schemas/atom">Front. Math. China, 8(5) (2013) 1001-16</arxiv:journal_ref>
      |    <link href="http://arxiv.org/abs/1212.2319v1" rel="alternate" type="text/html"/>
      |    <link title="pdf" href="http://arxiv.org/pdf/1212.2319v1" rel="related" type="application/pdf"/>
      |    <arxiv:primary_category xmlns:arxiv="http://arxiv.org/schemas/atom" term="nlin.SI" scheme="http://arxiv.org/schemas/atom"/>
      |    <category term="nlin.SI" scheme="http://arxiv.org/schemas/atom"/>
      |    <category term="39-04, 39A05, 39A14" scheme="http://arxiv.org/schemas/atom"/>
      |  </entry>
    """.stripMargin

  "entry" should "match" in {
    val xml = XML.loadString(entryString)
    val entry = Entry(xml)
    entry.id should be(new URL("http://arxiv.org/abs/1212.2319v1"))
    entry.title should be("Conservation laws of some lattice equations")
    val authors = entry.authors
    authors.length should be(2)
    authors.count(_.affiliation.isDefined) should be(0)
    val links = entry.links
    links.length should be(3)
    val categories = entry.categories
    categories.length should be(4)
    val arxivCats = categories.filter(_.isInstanceOf[ArxivCat])
    arxivCats.length should be(1)
    arxivCats.head.isPrimary should be(true)
  }

}
