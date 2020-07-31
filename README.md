# Token Generator Factory

### Description
This dependency is designed to work as a component that should used for dynamic token generation.

Providing a structure that allows the implementation of the request and response contracts (schema), it fits perfectly when applications need to create tokens from different external APIs (e.g. let`s suppose you have a microservice architecture and many need to generate a token. Your microservices will need to expose the request data and response schema and do not concern about the communication).

### Technologies
Below follow the technologies that were used to develop this dependency:

* Spring Boot;
* Spring Rest Client;
* Sprint Test (test);
* JUnit 5 (test);
* BDDMockito/Mockito (test).

### Usage
The current version requires the applications importing this dependency to implement a *TokenGenerationSchema* bean as its configuration. This is necessary to provide the schemas and data required to create token generation calls.

The *TokenGenerationSchema* is composed of three pieces:

* *HeaderModelRequest* - this interface represents the headers data. The fields of the class implementing this interface are used as keys on the token generation call. Their values are used as the headers values. *The implementation of this interface is optional as long as the call may not need any header*;

* *TokenModelRequest* - this interface represents the request body data. The fields (and its values) of the class implementing this interface are used as the body of the token generation call. *The implementation of this interface is optional as long as the call may not need a body*;

* *ResponseSchema* - this interface represents the response body _schema_. This class will be populated with the response of the token generation call. *The implementation of this interface is _mandatory_ as long as the call is expected to create a response (token or something like)*.

*Important*: the *TokenGenerationSchema* needs an *instance* of both *HeaderModelRequest* and *TokenModelRequest*. The third component (*ResponseSchema*)  should be indicated as a *class* because only the response definition is necessary on the dependency setup.

***

Bellow follows a fragment example of the *TokenGenerationSchema* configuration:

```
@Bean
public TokenGenerationSchema getTokenGenerationSchema() {
  TokenGenerationSchema tokenGenerationSchema = new TokenGenerationSchema(
          new HeaderModelRequestMock("headerA", 1), 
          new TokenModelRequestMock("campoA", 2, "campoC"), 
          ResponseSchemaMock.class);
  return tokenGenerationSchema;
}
```

Another *mandatory* configuration is the parameterization of the property *token.generation.url*. It will be used to call the API providing the token.
Bellow follows a fragment example of the *url* parameterization:

```
token.generation.url=http://localhost:8080/api/token
```

### Limitations
This dependency`s version is a MVP of the concept trying to be applied and lacks flexibility. Below follow the things that are not configurable at this moment:

* it accepts only JSON endpoints to generate the token;
* it does only *POST* calls to token generation APIs.


### Roadmap (future features)
Below follow the features/improvements that may be implemented in next releases:

* configuration flexibility
  * different method calls (e.g. GET)
  * multiple request/response format (e.g. XML)

* token caching
* multiple token generation call

