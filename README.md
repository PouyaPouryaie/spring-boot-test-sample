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


How to use:
in this project use postgresql, so if you want use it code, first of all <br>
Run postgres and create 'test' database, then write your config in application.yml file <br>
at test section, next run any test you want.

**NOTE:**
This project is being developed