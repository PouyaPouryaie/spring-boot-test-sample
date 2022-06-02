# SpringTest

write sample Test-class to usher how create test in spring. <br>
I use BDD design for test. the test contain mockTest, standaloneTest, restTest and ... 

1) control test section
    1) standalone test (how test specific class without create other class and context)
    2) use applicationContext for test (how test specific class with spring applicationContext)
    3) MockTest (how test specific class with springMockMvc)
    4) RestTest (how run whole application and use restTemplate for test)
2) repository test section
3) service test section
4) Integration Test section
   1) Create Test for all method of ProductController to test whole project
   2) use H2 as TestDataBase that divided production from test environment
   3) Define TestH2Repository as jpaRepo for test


How to use:
in this project use postgresql for production and H2 for test, so if you want use it code, <br> 
for production mode : Run postgres and create 'test' database, then write your config in application.yml file <br>
at test section just run test and create db and other thing by jpa and H2, just run any test as you want.

**NOTE:**
This project is being developed