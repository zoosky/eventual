package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.Celebrity
import models.Celebrity.CelebrityBSONReader
import models.Celebrity.CelebrityBSONWriter
import models.Celebrity.celebrityFormat
import models.Name
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
}
