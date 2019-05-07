#!/bin/sh

# build with ant
ant build

# change dirs
cd build/classes

# run
java sparkcog.demo.MazeRunner
