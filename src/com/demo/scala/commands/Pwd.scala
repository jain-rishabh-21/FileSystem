package com.demo.scala.commands
import com.demo.scala.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State = {
    state.setMessage(state.wd.path)
  }
}
