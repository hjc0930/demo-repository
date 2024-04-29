let cities = [];

// Create
function createCity(name, parentPath = '') {
  const city = {
    id: cities.length + 1,
    name,
    path: parentPath
      ? `${parentPath}.${cities.length + 1}`
      : `${cities.length + 1}`,
    createDate: new Date(),
    updateDate: new Date(),
  };
  cities.push(city);
  return city;
}

createCity('City 1');
createCity('City 2');
createCity('City 3', '1'); // City 3 is a sub-city of City 1
createCity('City 4', '1'); // City 4 is also a sub-city of City 1
createCity('City 5', '1.3'); // City 5 is a sub-city of City 3, which is a sub-city of City 1
console.log(cities);
function buildTree(cities, parentPath = '') {
  return cities
    .filter((city) => {
      if (parentPath === '') {
        return city.path.split('.').length === 1;
      } else {
        return (
          city.path.startsWith(`${parentPath}.`) &&
          city.path.split('.').length - 1 === parentPath.split('.').length
        );
      }
    })
    .map((city) => {
      const subPath = parentPath ? `${parentPath}.${city.id}` : `${city.id}`;
      return {
        ...city,
        children: buildTree(cities, subPath),
      };
    });
}
console.log(buildTree(cities));

/**
 *
 * [
    {
        "id": 1,
        "name": "City 1",
        "path": "1",
        "createDate": "2024-04-28T12:33:59.362Z",
        "updateDate": "2024-04-28T12:33:59.362Z",
        "children": [
              {
                "id": 3,
                "name": "City 3",
                "path": "1.3",
                "createDate": "2024-04-28T12:33:59.362Z",
                "updateDate": "2024-04-28T12:33:59.362Z",
                "children": [
                    {
                        "id": 5,
                        "name": "City 5",
                        "path": "1.3.5",
                        "createDate": "2024-04-28T12:33:59.362Z",
                        "updateDate": "2024-04-28T12:33:59.362Z",
                        "children": []
                    }
                ]
            },
        ]
    },
   {
        "id": 2,
        "name": "City 2",
        "path": "2",
        "createDate": "2024-04-28T12:33:59.362Z",
        "updateDate": "2024-04-28T12:33:59.362Z",
        "children": []
    },
    {
        "id": 4,
        "name": "City 4",
        "path": "1.4",
        "createDate": "2024-04-28T12:33:59.362Z",
        "updateDate": "2024-04-28T12:33:59.362Z",
        "children": []
    },
]
 *
 */
