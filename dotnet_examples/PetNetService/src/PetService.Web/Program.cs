using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using CoreService.Logging.File;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace PetService.Web
{
    public class Program
    {
        public static Task Main(string[] args) => CreateWebHostBuilder(args).Build().RunAsync();

        private static IWebHostBuilder CreateWebHostBuilder(string[] args)
        {
            // => WebHost.CreateDefaultBuilder(args).UseStartup<Startup>();

            var builder = new WebHostBuilder()
                .ConfigureAppConfiguration((context, config) =>
                {
                    config.AddJsonFile("appsettings.json", true, true)
                        .AddJsonFile($"appsettings.{context.HostingEnvironment.EnvironmentName}.json", true, true)
                        .AddEnvironmentVariables()
                        .AddCommandLine(args);
                })
                .ConfigureLogging((context, logging) =>
                {
                    logging.AddConfiguration(context.Configuration.GetSection("Logging"));
                    if (context.HostingEnvironment.IsDevelopment())
                    {
                        logging.AddDebug();
                    }

                    var settings = context.Configuration.GetSection("Logging:File").Get<FileLogSettings>();
                    logging.AddFile(settings);
                    logging.AddConsole();
                })
                .UseKestrel((builderContext, options) => options.Configure(builderContext.Configuration.GetSection("Kestrel")))
                .UseIIS()
                .UseIISIntegration()
                .UseContentRoot(Directory.GetCurrentDirectory())
                .UseDefaultServiceProvider((context, options) => options.ValidateScopes = context.HostingEnvironment.IsDevelopment())
                .UseStartup<Startup>();

            return builder;
        }
    }
}