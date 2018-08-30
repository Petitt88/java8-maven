using System.Collections.Generic;

namespace PetService.Web.Data
{
    public class Person
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Age { get; set; }
        
        public List<Car> Cars { get; set; }
    }
}