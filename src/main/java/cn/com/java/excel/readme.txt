一、poi操作excel的三种方式：SXSSF、eventmodel、usermodel

方式                              说明                                                读写性
SXSSF	        内存中保留一定行数数据，超过行数，将索引最低的数据刷入硬盘	                只写
eventmodel	    基于事件驱动,SAX的方式解析excel，cup和内存消耗低                         只读
usermodel	    传统方式，cpu和内存消耗大                                              可读可写