package controllers

import java.io.File

import play.Play
import play.api.mvc.Action
import play.api.mvc.Controller

/*
 * Author: Sari Haj Hussein
 */

object Application extends Controller {
  
  /** this method serves the index page app/views/index.scala.html */
  def index(any: String) = Action {
    Ok(views.html.index())
  }
  
  /** this method resolves "any" into the corresponding HTML page URI */
  def getURI(any: String): String = any match {
    case "main" => "/public/html/main.html"
    case _ => "error"
  }
  
  /** this method loads an HTML page from public/html */
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application().path()
    var file = new File(projectRoot + getURI(any))
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }
}