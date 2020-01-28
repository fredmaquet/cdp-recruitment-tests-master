# How I contributed to this project

First, before fixing the issues and adding new functionality, we decided to create a package structure in order to
separate <b><i>controller/service/repository</i></b> and avoid having everything on one package.

Then, we added all basic unit test.


## Identified Issues

 <b>1. Adding review does not work</b>

The goal was to make an update event on service with the method <i><b>save</b></i> on <b>CrudRepository</b> instead of <i><b>Repository</i></b>

 <b>2. Using the delete button works but elements comes back when i refresh the page</b> 

It was just to be able to delete on database 

<b>@Transactional(readOnly = true)</b> => <b>@Transactional</b> on EntityRepository


## New feature
Enable a new route for the API `/search/{query}`

We used stream and lambda expression in order to filter and recreate new list with filtered elements.

<b>Example of request : </b>

```sh
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/events/search/Wa'     
```

<b>Example of response : </b>

```sh
[
  {
    "id": 1000,
    "title": "GrasPop Metal Meeting [1]",
    "imgUrl": "img/1000.jpeg",
    "bands": [
      {
        "name": "Metallica [1]",
        "members": [
          {
            "name": "Queen Anika Walsh"
          }
        ]
      }
    ],
    "nbStars": null,
    "comment": null
  }
]
```


## To go further:
 We should add web-services tests with [karate](https://github.com/intuit/karate) and check quality of unit tests with [pitest](https://pitest.org/).