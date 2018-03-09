# Spark Streaming 
## checkpointing

Checkpointing in Spark is a feature that can be used in normal non-streaming Spark applications if the
execution graph is large enough to merit checkpointing in RDD. 
This serves to store its state so that it's
lineage need not be stored entirely in memory. However, checkpointing is generally used or even required
with certain types of transformations in streaming applications. 
There are two types of checkpointing operations. 

### metadata checkpointing. 

This is basically persisting configuration, DStream operations, and information about incomplete batches that
have yet to be processed. Don't confuse this with the ability to recover from received data that has not yet
been processed. 
For now, know that this mode of checkpointing specifically targets recovery from driver failures. 

If the driver fails and you don't have checkpointing enabled, then the entire DAG of DStream execution is
lost in addition to the understanding of state for executors. 
So metadata checkpointing helps Spark applications tolerate driver failures. Keep in mind that a driver failure actually also means that you
lose your executors, so restarting the driver is kind of like restarting your application from scratch
except that we use GET or CREATE on the Spark and streaming contexts so the driver will attempt to recover
the information it needs from a checkpoint and relaunch executors in their previous state. 

### Data Checkpointing
Data checkpointing is useful for stateful
transformations where data needs to be stored across batches. 
Window transformations and stateful
transformations like updateStateByKey and mapWithState, require this. 

Now you can checkpoint RDDs on your own, but simply using these transformations
and enabling checkpointing on the StreamingContext as we've done already and essentially
takes care of all we need to enable both metadata and data checkpointing alike. 

## Stateful Transformations

The Stateful Transformations responds to the question around our ability to maintain some state throughout
the entire application's life based on data received from a DStream. 
And Spark also provides means of maintaining state using either the `updateStateByKey` or `mapWithState` functions. 

We can think of a stateful transformation in Spark as some sort of global bag of information or global state 
that you have available to you to store information on the side. In reality, it's a distributed bag of
information so the physical implementation isn't restricted to what one machine can handle. 

This bag of information contains the global state that you want to track and has a type. 

A major difference between using statement transformations and a reduce or aggregation function, for example, 
is that the state can have a completely different type than the data it receives. 
