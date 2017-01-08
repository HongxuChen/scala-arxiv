package arxiv.api

// both exist or neither
case class SortOpt(sortBy: SortBy.Ty, sortOrder: SortOrder.Ty) {

  def toParams: ParamTy = Seq(
    "sortBy" -> sortBy.toString,
    "sortOrder" -> sortOrder.toString
  )
}

object SortBy extends Enumeration {
  type Ty = Value
  val relevance, lastUpdatedDate, submittedDate = Value
}

object SortOrder extends Enumeration {
  type Ty = Value
  val ascending, descending = Value
}
