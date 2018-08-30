namespace PetService.Web.Data
{
    public class Car
    {
        public int Id { get; set; }
        public string Type { get; set; }
        public string Engine { get; set; }

        public int PersonId { get; set; }
        public Person Person { get; set; }
    }
}