# Class Hierarchy Diagram
At the very top, the Scala collections are implemented using traits and abstract classes.  
To recap, traits are a fundamental unit of code we use, which means at the very top we will find the  
code that may be common or being reused by multiple collections.  
These are further organized by three types of collections, sequences, sets, and maps.  
Let us cover each one of them to know what they offer. 
## Sequences
Sequences are the type of collections or data structures where the ordering of the element matters.  
These types of sequences are further classified into IndexedSeq and LinearSeq.  
### IndexedSeq
IndexedSeq are the type of collection where we can address any element by its position or an index.  
For example, in a five element array, we can access the second element directly without going through  
index 0. 
### LinearSeq
LinearSeq, on the other hand, is a collection where the only way to get to a certain element is by  
traversing from the start. We do not know the indices of the elements inside the collection.  
For example, in link list, the only element we can access is head.  
The remaining structure is called tail, which is treated as an another link list. So the only way to  
access the second element in this case would be to ask for the head of the tail of the list.
## Sets
Sets are the type of collections where the element of the collection has to be unique.  
For example, the even numbers less than 10 would be 2, 4, 6, and 8. The sets are further classified  
into SortedSet and BitSet. 
### SortedSet
SortedSet are the type of collection where you can perform the ordering along with their uniqueness  
property. 
### BitSet
On the other hand, BitSet is a concrete class, not a trait, which keeps the set of non-negative integers  
represented as the variable-sized arrays. 
## Maps
Maps are the type of collections where the combination of key and values is stored, where keys are unique.  
This is further classified into SortedMap, which is a sorted version of a map where the keys are unique  
and they are sorted.  
There are a huge number of concrete classes which can be found under the Scala collections library.
