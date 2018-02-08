package com.concur.akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

//greeterCompanion

  object Greeter{
    def props(message: String, printerActor: ActorRef): Props = Props(new Greeter(message, printerActor))

    final case class whoToGreet(who: String)
    case object Greet

  }

  //Greeter Actor.
  class Greeter(message: String, printerActor: ActorRef) extends Actor{
    import Greeter._
    import Printer._

    var greeting = ""

    override def receive ={
      case whoToGreet(who) =>
        greeting = s"$message, $who"
      case Greet =>
        printerActor ! Greeting(greeting)

    }
  }

  //Printer Companion
  object Printer{
    //printer-messages
    def props: Props = Props[Printer]
    //printer-messages
    final case class Greeting(greeting:String)
  }

  //Printer Actor
  class Printer() extends Actor with ActorLogging {
    import Printer._

    override def receive = {
      case Greeting(greeting) =>
        log.info(s"Greeting received from ${sender()}): $greeting")
    }
  }

  //main-class

  object AkkaQuickStart extends App{
    import Greeter._

    //Create helloAkka Actor System.
    val system: ActorSystem = ActorSystem("helloAkka")

    //create actors
    //Create Printer actor

    val printerActor: ActorRef = system.actorOf(Printer.props, "PrinterActor")

    //Create Greeting actor.
    val namasteGreeter: ActorRef = system.actorOf(Greeter.props("Namaste", printerActor), "NamasteActor")
    val howdyGreeter: ActorRef = system.actorOf(Greeter.props("Howdy", printerActor), "HowdyActor")
    val helloGreeter: ActorRef = system.actorOf(Greeter.props("Hello", printerActor), "HelloActor")

    namasteGreeter ! whoToGreet("Akka")
    namasteGreeter ! Greet

    howdyGreeter ! whoToGreet("Chandra")
    howdyGreeter ! Greet

    helloGreeter ! whoToGreet("Nadukula")
    helloGreeter ! Greet

  }

