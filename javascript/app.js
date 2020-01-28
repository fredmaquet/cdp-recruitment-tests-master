var parsedJSON = require('./data.js');
var datas = JSON.parse(JSON.stringify(parsedJSON.data));

var datasFiltered = datas

process.argv.slice(2).forEach((arg, index) => {
    if (arg.includes('--filter=')) {
        datasFiltered = filterDatas(datas, arg.split('=')[1]);
        console.log(JSON.stringify(datasFiltered));
    }

    if (arg.includes('--count')) {
        count(datasFiltered);
        console.log(JSON.stringify(datasFiltered));
    }
})

function filterDatas(datas, filter) {
    var datasFiltered = datas.map(country => {
        var peopleFiltered = country.people.map(people => {
            // Create a new filtered array of animals
            var animalsFiltered = people.animals.filter(animal => animal.name.includes(filter));
            // Set the new array to new people
            people.animals = animalsFiltered;

        });
        var newCountry = country;
	// Purge people without animals
        newCountry.people = newCountry.people.filter(people => people.animals.length > 0);
        return newCountry;
    });
    // Purge country without people
    datasFiltered = datasFiltered.filter(country => country.people.length > 0);
    return datasFiltered;
}

function count(datas) {
    var newDatas = datas.map(country => {
        var newCountry = country;
        newCountry.name = country.name + " [" + country.people.length + "]";
        var newPeople = country.people.map(people => {
            var newPeople = people;
            newPeople.name = people.name + " [" + people.animals.length + "]";
            return newPeople;
        });
        newCountry.people = newPeople;
        return newCountry;
    });

    return newDatas;
}


module.exports = {
    filterDatas,
    count
}
