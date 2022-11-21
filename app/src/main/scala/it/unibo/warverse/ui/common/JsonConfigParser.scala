package it.unibo.warverse.ui.common

import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import it.unibo.warverse.model.world.World.Citizen
import it.unibo.warverse.model.common.Geometry
import it.unibo.warverse.model.fight.Army.*
import it.unibo.warverse.model.fight.Army
import it.unibo.warverse.model.world.World.Country
import it.unibo.warverse.model.common.Geometry.Polygon
import it.unibo.warverse.model.common.Geometry.Polygon2D
import it.unibo.warverse.model.common.Geometry.Point2D
import javax.swing.JOptionPane
import scala.language.postfixOps
import util.control.Breaks.*

class JsonConfigParser(jsonFile: String):

  var countriesToReturn: List[Country] = _
  val jsonObj: JObject = parse(jsonFile).asInstanceOf[JObject]

  if verifyJson(jsonObj) then
    val countries = (jsonObj \ "Countries").asInstanceOf[JArray]

    countriesToReturn = countries.arr.map(country =>
      val name = (country \ "name").values

      val citizen =
        (country \ "citizen").values.asInstanceOf[List[Map[String, BigInt]]]
      val citizenToPush: List[Citizen] = citizen.map(el =>
        val x: Double = extractData(el.get("x")).asInstanceOf[Double]
        val y: Double = extractData(el.get("y")).asInstanceOf[Double]
        Citizen(Geometry.Point2D(x, y))
      )

      val precisionUnit =
        (country \ "armyUnits" \ "precisionUnit").asInstanceOf[JArray]
      val areaUnit =
        (country \ "armyUnits" \ "areaUnit").asInstanceOf[JArray]

      val precisionUnitPass: List[PrecisionArmyUnit] =
        precisionUnit.arr.map(el =>
          val name = (el \ "name").values.toString
          val chanceOfHit = (el \ "chanceOfHit").values.toString.toDouble
          val rangeOfHit = (el \ "rangeOfHit").values.toString.toDouble
          val availableHits = (el \ "availableHits").values.toString.toInt
          val dailyConsume = (el \ "dailyConsume").values.toString.toDouble
          val speed = (el \ "speed").values.toString.toDouble
          val position =
            (el \ "position").values.asInstanceOf[Map[String, BigInt]]
          val x: Double = extractData(position.get("x")).asInstanceOf[Double]
          val y: Double = extractData(position.get("y")).asInstanceOf[Double]
          PrecisionArmyUnit(
            name,
            chanceOfHit,
            rangeOfHit,
            availableHits,
            dailyConsume,
            speed,
            Geometry.Point2D(x, y)
          )
        )

      val areaUnitPass: List[AreaArmyUnit] = areaUnit.arr.map(el =>
        val name = (el \ "name").values.toString
        val chanceOfHit = (el \ "chanceOfHit").values.toString.toDouble
        val rangeOfHit = (el \ "rangeOfHit").values.toString.toDouble
        val availableHits = (el \ "availableHits").values.toString.toInt
        val dailyConsume = (el \ "dailyConsume").values.toString.toDouble
        val speed = (el \ "speed").values.toString.toDouble
        val position =
          (el \ "position").values.asInstanceOf[Map[String, BigInt]]
        val x: Double = extractData(position.get("x")).asInstanceOf[Double]
        val y: Double = extractData(position.get("y")).asInstanceOf[Double]
        val areaOfImpact = (el \ "areaOfImpact").values.toString.toDouble
        AreaArmyUnit(
          name,
          chanceOfHit,
          rangeOfHit,
          availableHits,
          dailyConsume,
          speed,
          Geometry.Point2D(x, y),
          areaOfImpact
        )
      )
      val armyList: List[Army.ArmyUnit] = areaUnitPass ++ precisionUnitPass

      val resources = (country \ "resources").values.toString.toDouble

      val boundariesTmp =
        (country \ "boundaries").values.asInstanceOf[List[Map[String, BigInt]]]

      val pointList: List[Point2D] = boundariesTmp.map(el =>
        val x: Double = extractData(el.get("x")).asInstanceOf[Double]
        val y: Double = extractData(el.get("y")).asInstanceOf[Double]
        Geometry.Point2D(x, y)
      )
      val boundaries: Polygon2D = Polygon2D(pointList)

      JOptionPane.showMessageDialog(
        null,
        "Configuration uploaded successfully."
      )
      Country(
        name.toString,
        citizenToPush,
        armyList,
        resources,
        boundaries
      )
    )
  else
    JOptionPane.showMessageDialog(
      null,
      "Found problems in configuration file."
    )

  def verifyJson(jObject: JObject): Boolean =
    if !checkHeader(jObject, "Countries") then return false
    var verify = true
    breakable {
      (jObject \ "Countries")
        .asInstanceOf[JArray]
        .arr
        .foreach(country =>
          verify = checkHeader(country, "name") &&
            checkHeader(country, "citizen") &&
            checkPointsPosition(country, "citizen")
          checkHeader(country, "armyUnits") &&
          checkHeader(country, "resources") &&
          checkHeader(country, "boundaries") &&
          checkPointsPosition(country, "boundaries") &&
          checkHeader(country, "name") &&
          checkHeader(country, "armyUnits", "precisionUnit") &&
          checkHeader(country, "armyUnits", "areaUnit") &&
          checkArmy(country \ "armyUnits" \ "precisionUnit", false) &&
          checkArmy(country \ "armyUnits" \ "areaUnit", true)
          if !verify then
            println("----ISSUE WITH JSON CONFIGURATION----")
            break
        )
    }
    verify

  def checkHeader(jValue: JValue, header: String): Boolean =
    if (jValue \ header) == JNothing then
      println("----ISSUE WITH ***" + header + "*** CONFIGURATION----")
    (jValue \ header) != JNothing

  def checkHeader(jValue: JValue, header: String, subHeader: String): Boolean =
    if (jValue \ header \ subHeader) == JNothing then
      println("----ISSUE WITH ***" + header + "*** CONFIGURATION----")
    (jValue \ header \ subHeader) != JNothing

  def checkPointsPosition(jValue: JValue, header: String): Boolean =
    val points =
      (jValue \ header).values.asInstanceOf[List[Map[String, BigInt]]]
    points
      .map(el =>
        val x: Double = extractData(el.get("x")).asInstanceOf[Double]
        val y: Double = extractData(el.get("y")).asInstanceOf[Double]
        (x >= 0 && x <= 1050) && (y >= 0 && y <= 700)
      )
      .forall(_ == true)

  def checkUnitPosition(jValue: JValue): Boolean =
    val pos = (jValue \ "position").values.asInstanceOf[Map[String, BigInt]]
    val x: Double = extractData(pos.get("x")).asInstanceOf[Double]
    val y: Double = extractData(pos.get("y")).asInstanceOf[Double]
    (x >= 0 && x <= 1050) && (y >= 0 && y <= 700)

  def checkArmy(jValue: JValue, isArea: Boolean): Boolean =
    val checker = jValue.asInstanceOf[JArray]
    var verify = true
    breakable {
      checker.arr
        .map(soldier =>
          verify = checkHeader(soldier, "name") &&
            checkHeader(soldier, "chanceOfHit") &&
            checkHeader(soldier, "rangeOfHit") &&
            checkHeader(soldier, "availableHits") &&
            checkHeader(soldier, "dailyConsume") &&
            checkHeader(soldier, "speed") &&
            checkHeader(soldier, "position")
            && checkUnitPosition(soldier)
            && (
              if isArea then checkHeader(soldier, "areaOfImpact")
              else true
            )
          if !verify then
            println("----ISSUE WITH SOLDIER CONFIGURATION----")
            break
        )
    }
    verify

  def getConfig: Array[Country] = this.countriesToReturn.toArray

  def extractData(x: Option[BigInt]): Any = x match
    case Some(s) => s.toDouble
    case None    => "?"
