# Rabo Payment
    Rabo Payment validator help to validate customer statement and Application running in port number 8000.

# API
  > POST -  http://localhost:8000/api/v1/statement/validate
  * Help to validate customer statement

# To Run
 > URL: POST http://localhost:8000/swagger-ui.html#/customer-statement-controller-impl
> Input  Json:
```sh

    [
           {
            "Reference": 194261,
            "AccountNumber": "NL91RABO0315273637",
            "Description": "Clothes from Jan Bakker",
            "Start Balance": 21.6,
            "Mutation": -41.83,
            "End Balance": -20.23
          },
          {
            "Reference": 194261,
            "AccountNumber": "NL27SNSB0917829871",
            "Description": "Clothes for Willem Dekker",
            "Start Balance": 92.23,
            "Mutation": "+15.57",
            "End Balance": 106.8
          }
        ]
```