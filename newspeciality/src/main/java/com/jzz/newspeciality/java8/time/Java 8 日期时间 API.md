# Java 8 日期时间 API

Java 8引入了基于IS0-8601日期时间标准的新的Date-Time API。

Date-Time API使用ISO-8601中定义的日历系统作为默认日历。此日历基于公历系统

Date-Time API中的LocalDateTime，ZonedDateTime和OffsetDateTime都使用ISO日历系统。

Date-Time API使用Unicode公共区域设置数据存储库（CLDR）作为区域设置数据。

Date-Time API使用时区数据库（TZDB）获取有关时区的信息。

## 日期时间API设计原则

API中的方法定义明确

Java Date-Time API主要由不可变类组成。创建对象后，无法修改它。要更改其值，必须将新对象创建为原始对象的修改副本。

Date-Time API是线程安全的。

Date-Time API尽可能是可扩展的。

## 日期时间包

Java Date-Time API由主包`java.time`和四个子包组成：

- `java.time`包含用于表示日期和时间的API的核心。它包括日期，时间，日期和时间组合，时区，时刻，持续时间和时钟的类。` LocalDate, LocalTime, LocalDateTime, ZonedDateTime, Period, Duration, and Instant`课程在这个包中。此包中的类基于ISO-8601标准。
- `java.time.chrono`包含用于表示除默认ISO-8601之外的日历系统的API。例如，Hijrah日历，ThaiBuddhist日历等。
- `java.time.format` 包含用于格式化和解析日期和时间的类。
- `java.time.temporal`包含API，允许日期和时间类之间的互操作，查询和调整。例如，TemporalField和ChronoField以及TemporalUnit和ChronoUnit。
- `java.time.zone`包含支持时区，时区偏移和时区规则的类。例如，ZonedDateTime，ZoneId或ZoneOffset。

# Java日期时间方法

java Date-Time API中的方法名称尽可能在类之间保持一致。



| 方法   | 方法类型     | 描述                                                    |
| ------ | ------------ | ------------------------------------------------------- |
| of     | 静态工厂方法 | 工厂方法创建实例并验证输入参数                          |
| from   | 静态工厂方法 | 将输入参数转换为目标类的实例                            |
| parse  | 静态工厂方法 | 解析输入字符串以创建目标类的实例。                      |
| now    | 静态工厂方法 | 获取当前时间                                            |
| format | 实例方法     | 指定DateTimeFormatter格式格式化属性对象，以生成字符串。 |
| get    | 实例方法     | 返回目标对象的一部分。                                  |
| is     | 实例方法     | 查询目标对象。                                          |
| with   | 实例方法     | 返回目标对象的副本，其中一个元素已更改。                |
| plus   | 实例方法     | 返回对象的副本，并添加一定的时间。                      |
| minus  | 实例方法     | 返回对象的副本，减去时间量。                            |
| to     | 实例方法     | 将此对象转换为另一种类型。                              |
| at     | 实例方法     | 将此对象与另一个对象组合                                |
|        |              |                                                         |

