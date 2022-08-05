package com.demo.scala.filesystem

import com.demo.scala.commands.Command
import com.demo.scala.files.Directory

import java.util.Scanner

object FileSystem extends App{

  val root: Directory = Directory.ROOT
  var state: State = State(root, root)
  val scanner = new Scanner(System.in)

  while(true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }
}
