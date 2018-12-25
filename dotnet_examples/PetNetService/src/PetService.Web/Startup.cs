using CoreService;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Diagnostics;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using PetService.Web.Data;

namespace PetService.Web
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<CookiePolicyOptions>(options =>
            {
                // This lambda determines whether user consent for non-essential cookies is needed for a given request.
                options.CheckConsentNeeded = context => true;
                options.MinimumSameSitePolicy = SameSiteMode.None;
            });

            services.AddDbContext<ApplicationDbContext>(options =>
                //options.UseSqlite(Configuration.GetConnectionString("SqliteConnection"))
                options.UseSqlServer(Configuration.GetConnectionString("SqlServerConnection"))
                    .ConfigureWarnings(warnings => warnings.Throw(RelationalEventId.QueryClientEvaluationWarning))
            );

            services.AddDefaultIdentity<IdentityUser>().AddEntityFrameworkStores<ApplicationDbContext>();

            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_2);

            services.AddTransient<TelemetryService>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            var applicationPath = Configuration.GetValue<string>("App:ApplicationPath");

            /* it seems there are 2 solutions to use a subfolder application path instead of the root:
             1. just use app.UsePathBase(applicationPath);
                - this affects both the routing and serving static files
             
             2. a) app.UseStaticFiles(applicationPath); 
                    - for serving static files: request must start with /pet-app --> then Kestrel serves the file from the wwwroot folder
                b) app.Map(applicationPath, mainApp => { mainApp.UseMvc(); });
                    - for routing: controller routes must start with /pet-app 
                
                app.UsePathBase() is much more likable.
            */

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                // if the EF model and the database schema are out of sync, as soon as a database interaction is made display an error page which
                // let the user execute the missing migrations
                app.UseDatabaseErrorPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
                app.UseHsts();
            }

            app.UseHttpsRedirection();

            app.UsePathBase(applicationPath);
            //app.UseStaticFiles(applicationPath);    // required for the 2nd applicationPath solution
            app.UseStaticFiles();

            app.UseCookiePolicy();

            app.UseAuthentication();

            //app.Map(applicationPath, mainApp => { mainApp.UseMvc(); });    // required for the 2nd applicationPath solution
            app.UseMvc();
        }
    }
}