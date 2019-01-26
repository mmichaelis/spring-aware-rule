-----

**Archived:** Solution is meanwhile available in Spring. No need for copy &amp; paste code anymore.

-----

Spring-Aware-Rule
=================

An example how to replace (most) functionality of a SpringJUnit4ClassRunner by a JUnit TestRule as introduced in [CoreMedia Minds Post][minds-post]

Mind that the rule copies some (and not all) behavior of the SpringJUnit4ClassRunner. So the normal restrictions for copy & paste code also applies to this solution.

**Note:** There is a more complete solution already waiting as [Pull Request 222][spring-framework-pr-222] for the spring-framework addressing this issue which is also described in [SPR-7731][].

[minds-post]: <http://minds.coremedia.com/2014/08/28/junit-runwith-springjunit4classrunner-vs-parameterized/> "JUnit-@RunWith: SpringJUnit4ClassRunner vs. Parameterized | Minds"
[spring-framework-pr-222]: <https://github.com/spring-projects/spring-framework/pull/222> "SPR-10217 Implement JUnit 4 Support using Rules by marschall · Pull Request #222"
[SPR-7731]: <https://jira.spring.io/browse/SPR-7731> "[SPR-7731] Provide @Rule alternative to SpringJUnit4ClassRunner - Spring JIRA"
