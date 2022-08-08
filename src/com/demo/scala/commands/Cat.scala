package com.demo.scala.commands
import com.demo.scala.filesystem.State

class Cat(fileName: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd
    val dirEntry = wd.findEntry(fileName)

    if (dirEntry == null)
      state.setMessage(fileName + ": No Such File!")
    else if (dirEntry.isDirectory)
      state.setMessage(fileName + ": Not a file!")
    else {
      state.setMessage(dirEntry.asFile.contents)
    }
  }
}
