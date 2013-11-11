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
/*
  /** retrieve the celebrity for the given id as JSON */
  def show(id: String) = Action(parse.empty) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      // get the celebrity having this id (there will be 0 or 1 result)
      val futureCelebrity = collection.find(BSONDocument("_id" -> objectID)).one[Celebrity]
      futureCelebrity.map { celebrity => Ok(Json.toJson(celebrity)) }
    }
  }

  /** create a celebrity from the given JSON */
  def create() = Action(parse.json) { request =>
    Async {
      val first = request.body.\("first").toString() // get the fields from the request
      val last = request.body.\("last").toString()
      val name = Name(first, last)
      val website = request.body.\("website").toString()
      val celebrity = Celebrity(Option(BSONObjectID.generate), name, website) // create the celebrity
      collection.insert(celebrity).map(
        _ => Ok(Json.toJson[Celebrity](celebrity))) // return the created celebrity in a JSON
    }
  }

  /** update the celebrity for the given id from the JSON body */
  def update(id: String) = Action(parse.json) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      val first = request.body.\("first").toString() // get the fields from the request
      val last = request.body.\("last").toString()
      val name = Name(first, last)
      val website = request.body.\("website").toString()
      val modifier = BSONDocument( // create the modifier celebrity
        "$set" -> BSONDocument(
          "name" -> name,
          "website" -> website))
      collection.update(BSONDocument("_id" -> objectID), modifier).map(
        _ => Ok(Json.toJson[Celebrity](CelebrityBSONReader.read(modifier)))) // return the modified celebrity in a JSON 
    }
  }

  /** delete a celebrity for the given id */
  def delete(id: String) = Action(parse.empty) { request =>
    Async {
      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      collection.remove(BSONDocument("_id" -> objectID)).map( // and remove the celebrity
        _ => Ok).recover { case _ => InternalServerError }
    }
  }*/
}
