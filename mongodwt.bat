echo "Wired Tiger storage engine.. Compresses, optimistic locking at doc level, can be faster, but if in place updates occur then probably slower since it writes new Docuemnt each time." 
"c:\Program Files\MongoDB\Server\3.4\bin\mongod" --dbpath=c:\apps\mongoDBStoreWiredTiger --storageEngine wiredTiger