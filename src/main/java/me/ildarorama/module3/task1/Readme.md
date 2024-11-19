Java 17
HashMap throws with ConcurrentModificationException
Collections.synchronizedMap throws with ConcurrentModificationException
ConcurrentHashMap works fine because has separate view for values()
performance around one insert per 500ns
ThreadSafeMap works fine because has separate view for values() as well.
performance around one insert per 500ns

