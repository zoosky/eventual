package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.Celebrity
import models.Celebrity.celebrityFormat
import models.Celebrity.CelebrityBSONReader
import models.Celebrity.CelebrityBSONWriter
import models.Name
import models.Name.nameFormat
import models.Name.NameBSONWriter
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentIdentity
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer

/*
 * Author: Sari Haj Hussein
 */

object Celebrities extends Controller with MongoController {
  val collection = db[BSONCollection]("celebrities")

  /** list all celebrities */
  def index = Action { implicit request =>
    Async {
      val cursor = collection.find(
        BSONDocument(), BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
      val futureList = cursor.toList // convert it to a list of Celebrity
      futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
    }
  }
  
  /** create a celebrity from the given JSON */
  def create() = Action(parse.json) { request =>
    Async {
      val nameJSON = request.body.\("name")
      val name = nameFormat.reads(nameJSON).get
      val website = request.body.\("website").toString().replace("\"", "")
      val celebrity = Celebrity(Option(BSONObjectID.generate), name, website) // create the celebrity
      collection.insert(celebrity).map(
        _ => Ok(Json.toJson[Celebrity](celebrity))) // return the created celebrity in a JSON
    }
  }
}
