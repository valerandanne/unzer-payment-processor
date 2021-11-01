## Assignment

Build a small REST API which processes payments. Payments have an id and they can be approved or canceled through the provided 3rd Party Payment Processing API.

### Requirements

We would like you to implement the following three User Stories:

1. As a User I want to create a payment so that I retrieve an approval code.
2. As a User I want to cancel an existing payment based on a given payment id.
3. As a User I want to search for a payment by a given payment id so that I can see the status of the payment.

### Development

- We expect a solution written in Java (Version >= 11)/Kotlin, because that´s what we speak in our team
- Follow industry standards when building your API (REST principles, think in resources, use proper HTTP verbs, etc.)
- To run the provided 3rd Party Payment Processing API, see the README in ./external-processor-api

### Delivery

- Ideally you provide your solution in a github repository
- Feel free to briefly document your decision process

### Tips

- Keep it simple, clean and easy


## Solution

### Models
Definition of the models: 
- I decided to either not save nor show sensitive information regarding the payment. This was
thought out of simplicity as I didn't need to operate with the card number, expiry date and cvc.
- The card information could have been abstracted as another class (CardDetails), for time reasons I left it like it is.
- I had no time to add validations (I would have used javax validators)
- I intended to use lombok (to reduce boilerplate code but there was something wrong with my IDE)

### Payment Service

- If the communication with the external processor fails, the payment is processed and created but marked as with status
  FAILED. This is because from our side there has been no issues, and the payment request could be processed.
  
### General comments
- I used an H2 database in memory DB.
- I only did integration tests, I prioritized these over unit tests (due to lack of time)
- A better error response could be created for the custom exception
- Thanks for the patience
