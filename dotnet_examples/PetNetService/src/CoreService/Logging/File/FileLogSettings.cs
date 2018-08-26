using System.ComponentModel.DataAnnotations;

namespace CoreService.Logging.File
{
    public class FileLogSettings
    {
        [Required]
        public string FilePath { get; set; }
    }
}