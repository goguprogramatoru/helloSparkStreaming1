name := "SparkStreaming"

version := "1.0"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.6.1",
	"org.apache.spark" % "spark-streaming_2.10" % "1.6.1"
)