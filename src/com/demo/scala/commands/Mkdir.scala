package com.demo.scala.commands
import com.demo.scala.files.{DirEntry, Directory}
import com.demo.scala.filesystem.State

class Mkdir(name: String) extends CreateEntry(name ) {
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
