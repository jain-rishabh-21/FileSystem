package com.demo.scala.commands
import com.demo.scala.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State =
    state.setMessage("Unknown Command!")
}
