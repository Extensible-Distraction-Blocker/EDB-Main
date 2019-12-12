# Extensible Distraction Blocker
플러그인을 통해 잠금 정책의 확장이 가능하며, REST API를 통해 외부 서비스의 연동을 할 수 있는 잠금 프로그램의 구조를 마련한다.

시연 영상 링크 : 

## 주요 아키텍처
image 추가 예정

이 프로젝트는 plugin 구조를 통해 잠금 정책을 확장할 수 있는것을 목적으로 한다.

확장 가능한 지점인 EDBPlugin클래스는 pf4j의 동적 로드의 대상임을 의미하는 ExtensionPoint Interface를 구현한다.

구체적인 잠금정책들은 EDBPlugin class를 상속함으로서 구현될 수 있다. 구현된 클래스들은 각 잠금정책의 생명주기를 관리한다.

EDBPlugin들은 pf4j에서 정의된 DefaultPluginManager를 이용해 동적 로드될 수 있으며, 동적 로드된 후에는 EDBPluginManager에서 관리된다.

각각의 EDBPlugin들은 플러그인 설정과 동작에 이용되는 데이터/메소드인 집합인 PluginLogic들을 여러개 가질 수 있다.

---
image 추가 예정

UI로부터의 요청은 MainComponent에 대한 요청을 정의해둔 UIEventHandler의 메소드를 호출한다.

MainComponent의 내부 변화로 UI의 변경이 필요할 때, MainComponent가 직접적으로 구체적인 Controller에 대한 조작을 가한다면, MainComponent는 변경 가능성이 높은 UIComponent에 의존하게 되는 문제가 발생한다.

이를 방지하기 위해 추상화계층인 UIManipulator를 이용해 구체적으로 어떤 Controller, UI가 있는지 알지 못하더라도 UI에 대해 필요한 갱신을 요청할 수 있도록 한다.

웹으로의 요청과 응답도 비슷한 목적을 달성하기 위해 추상화 계층인 ServerRequester를 운용한다.
