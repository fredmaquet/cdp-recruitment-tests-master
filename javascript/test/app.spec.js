var assert = require('assert')
var app = require('../app')

const data = [
 {
    name: 'Tohabdal',
    people:
      [
        {
          name: 'Alexander Fleury',
          animals:
            [{name: 'Gelada'},
              {name: 'Rattlesnake'},
              {name: 'Woodpecker'}]
        },
        {
          name: 'Randall Benoît',
          animals:
            [{name: 'Chameleons'},
              {name: 'Sand Cat'}]
        }]
  },
  {
    name: 'Uzuzozne',
    people:
      [{
        name: 'Harold Patton',
        animals:
          [{name: 'Bearded Dragon'},
           {name: 'Shortfin Mako Shark'}]
       },
       {
        name: 'Georgia Hooper',
         animals:
           [{name: 'Grasshopper'},
             {name: 'Jackal'},
             {name: 'Zebu'}]
       },
       {
        name: 'Lillie Abbott',
        animals:
          [{name: 'John Dory'},
            {name: 'Gayal'},
            {name: 'Hawk'},
            {name: 'Umbrella Squid'},
            {name: 'Hyrax'},
            {name: 'Henkel\'s Leaf-tailed Gecko'}]
       }]
  },
{
    name: 'Satanwi',
    people:
      [{
        name: 'Elmer Kinoshita',
        animals:
          [{name: 'Weasel'},
           {name: 'Courser'}]
        },
        {
        name: 'Anthony Bruno',
        animals:
           [{name: 'Caracal'},
            {name: 'Anteater'},
            {name: 'Tarantula'},
            {name: 'Oryx'},
	    {name: 'John Dory'}]
        }]
}
]


describe('tests on filtering datas', function() {

    it('should filter datas with many animals on people', function() {
        var result = app.filterDatas(data, 'ry');
        assert.equal(result.length, 2);

        assert.equal(result[0].name, "Uzuzozne");
        assert.equal(result[0].people.length, 1);
        assert.equal(result[0].people[0].name, "Lillie Abbott");
        assert.equal(result[0].people[0].animals.length, 1);
        assert.equal(result[0].people[0].animals[0].name, "John Dory");

        assert.equal(result[1].name, "Satanwi");
        assert.equal(result[1].people.length, 1);
        assert.equal(result[1].people[0].name, "Anthony Bruno");
        assert.equal(result[1].people[0].animals.length, 2);
        assert.equal(result[1].people[0].animals[0].name, "Oryx");
	assert.equal(result[1].people[0].animals[1].name, "John Dory");
    });

    it('should filter and count datas many animals on people', function() {
        var result = app.count(app.filterDatas(data, 'ry'));
        assert.equal(result.length, 2);

        assert.equal(result[0].name, "Uzuzozne [1]");
        assert.equal(result[0].people.length, 1);
        assert.equal(result[0].people[0].name, "Lillie Abbott [1]");
        assert.equal(result[0].people[0].animals.length, 1);
        assert.equal(result[0].people[0].animals[0].name, "John Dory");

        assert.equal(result[1].name, "Satanwi [1]");
        assert.equal(result[1].people.length, 1);
        assert.equal(result[1].people[0].name, "Anthony Bruno [2]");
        assert.equal(result[1].people[0].animals.length, 2);
        assert.equal(result[1].people[0].animals[0].name, "Oryx");
	assert.equal(result[1].people[0].animals[1].name, "John Dory");

    });

    it('should filtered datas with unknown matching filter', function() {
        var result = app.filterDatas(data, 'NO MATCHING FILTER');
        assert.equal(result.length, 0);
    });
});


