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

class JsonConfigParser(jsonFile: String):

  val jsonObj = parse(jsonFile).asInstanceOf[JObject]
  val countries = (jsonObj \ "Countries").asInstanceOf[JArray]

  var countriesToReturn: List[Country] = countries.arr.map(country =>
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
        val name = (el \ "name").values.toString()
        val chanceOfHit = (el \ "chanceOfHit").values.toString().toDouble
        val rangeOfHit = (el \ "rangeOfHit").values.toString().toDouble
        val availableHits = (el \ "availableHits").values.toString().toInt
        val dailyConsume = (el \ "dailyConsume").values.toString().toDouble
        val speed = (el \ "speed").values.toString().toDouble
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
      val name = (el \ "name").values.toString()
      val chanceOfHit = (el \ "chanceOfHit").values.toString().toDouble
      val rangeOfHit = (el \ "rangeOfHit").values.toString().toDouble
      val availableHits = (el \ "availableHits").values.toString().toInt
      val dailyConsume = (el \ "dailyConsume").values.toString().toDouble
      val speed = (el \ "speed").values.toString().toDouble
      val position =
        (el \ "position").values.asInstanceOf[Map[String, BigInt]]
      val x: Double = extractData(position.get("x")).asInstanceOf[Double]
      val y: Double = extractData(position.get("y")).asInstanceOf[Double]
      val areaOfImpact = (el \ "areaOfImpact").values.toString().toDouble
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

    val resources = (country \ "resources").values.toString().toDouble

    val boundariesTmp =
      (country \ "boundaries").values.asInstanceOf[List[Map[String, BigInt]]]

    val pointList: List[Point2D] = boundariesTmp.map(el =>
      val x: Double = extractData(el.get("x")).asInstanceOf[Double]
      val y: Double = extractData(el.get("y")).asInstanceOf[Double]
      Geometry.Point2D(x, y)
    )
    val boundaries: Polygon2D = Polygon2D(pointList)

    Country(
      name.toString(),
      citizenToPush,
      armyList,
      resources,
      boundaries
    )
  )

  def getConfig(): Array[Country] = this.countriesToReturn.toArray

  def extractData(x: Option[BigInt]) = x match
    case Some(s) => s.toDouble
    case None    => "?"
