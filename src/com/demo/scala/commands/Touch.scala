package com.demo.scala.commands
import com.demo.scala.files.{DirEntry, File}
import com.demo.scala.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
