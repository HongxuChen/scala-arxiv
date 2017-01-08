package arxiv.api

import scala.util._

sealed trait QInterface {
  def key: String
}

/////////////////////////////////////////////////////////////////////

case class QueryParam(qFields: QField*) extends QInterface {
  val key = "search_query"

  override def toString: String = qFields.mkString(" ")
}

object QueryParam {
  def apply(qFields: List[QField]): QueryParam = QueryParam(qFields: _*)
}

object QueryOp extends Enumeration {
  type Ty = Value
  val AND, OR, ANDNOT = Value
}

case class QField(op: QueryOp.Ty, prefix: String, content: String) {
  val legalPrefix = Set("ti", "au", "abs", "co", "jr", "cat", "rn", "id", "all")
  require(legalPrefix(prefix))

  override def toString: String = op match {
    case QueryOp.AND => s"${prefix}:${content}"
    case _ => s"${op}+${prefix}:${content}"
  }
}

object QField {
  def apply(prefix: String, content: String): QField = QField(QueryOp.AND, prefix, content)
}

/////////////////////////////////////////////////////////////////////

case class IDParam(ids: List[String]) extends QInterface {
  val key = "id_list"

  def this(s: String) = this(s.split(",").toList)

  override def toString: String = s"${ids.mkString(",")}"
}


case class StartParam(v: Int) extends QInterface {
  val key = "start"

  def this(s: String) = this {
    Try(s.toInt).toOption.getOrElse(defaultStart)
  }

  override def toString: String = s"${v}"
}

case class MaxResParam(v: Int) extends QInterface {
  val key = "max_results"

  def this(s: String) = this {
    Try(s.toInt).toOption.getOrElse(defaultMaxRes)
  }

  override def toString: String = s"${v}"
}