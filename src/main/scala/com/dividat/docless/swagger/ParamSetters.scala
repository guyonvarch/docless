package com.dividat.docless.swagger

trait ParamSetters[T] {
  def withParams(param: OperationParameter*): T
}
