/**
 * Created by mourad.benabdelkerim on 5/4/16.
 */
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Application {
	def main(args: Array[String]) {
		if (args.length < 2) {
			System.err.println("Usage: sparkStreaming <hostname> <port>")
			System.exit(1)
		}

		// Create the context with a 1 second batch size
		val sparkConf = new SparkConf().setMaster("local[2]").setAppName("sparkStreaming")
		val ssc = new StreamingContext(sparkConf, Seconds(5))

		// Create a socket stream on target ip:port and count the
		// words in input stream of \n delimited text (eg. generated by 'nc')
		// Note that no duplication in storage level only for running locally.
		// Replication necessary in distributed scenario for fault tolerance.
		try{
		val lines = ssc.socketTextStream(args(0), args(1).toInt, StorageLevel.MEMORY_AND_DISK_SER)
		val words = lines.flatMap(_.split(" "))
		val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
		//val wordCounts = words.map(x => (x,1)).reduceByKeyAndWindow((a:Int,b:Int) => (a + b), Seconds(15), Seconds(5))
		wordCounts.print()
		ssc.start()
		ssc.awaitTermination()
		}
		catch{
			case  e: java.net.ConnectException =>{}
		}
	}

}
