# DBDirectory  
 If you know Chinese, Please skip to the last  
   
     
     
 Database Direcotry Implementation Of Lucene Directory , Compatible __Lucene4.7__  
 Modified by [unkascrack/lucene-databasedirectory](https://github.com/unkascrack/lucene-databasedirectory)  ,__This is compatible Lucene5.x__
 
 
   
     
  This is __not__ Efficiency way (No RAM Cache, No FileSystem Cache, Direct Access DB), It's mainly used for __Index Data Synchronous__
  If you really need this way, you can try  
  Pass test of Lucene 4.7.2/4.7.1  
    
      
      
  Lucene 4.7 的数据库Directory实现,效率不高,没办法的实现, 给实在有需求的人用  
  
  不要用来搜索,数据量大的时候,性能低下,主要用来备份同步索引数据
