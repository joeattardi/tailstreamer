module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON("package.json"),
        bower: {
            install: { }
        },
        concat: {
            js: {
                src: ["lib/**/*.js", "src/main/resources/static/js/**/*.js"],
                dest: "build/resources/main/static/js/<%= pkg.name %>.js"
            }
        },
        uglify: {
            dist: {
                files: {
                    "build/resources/main/static/js/<%= pkg.name %>.js": ["<%= concat.js.dest %>"]
                }
            }
        },
        less: {
            dist: {
                options: {
                    paths: ["dist"],
                    cleancss: true
                },
                files: {
                    "build/resources/main/static/css/style.css": "src/main/resources/static/less/style.less"
                }
            }
        },
        jshint: {
            files: ["Gruntfile.js", "src/main/resources/static/js/tailstreamer.js"]            
        },
        clean: [
            "build/resources/main/static/css/style.css", "build/resources/main/static/js/tailstreamer.js"
        ],
        watch: {
            less: {
                files: ["src/main/resources/static/less/style.less"],
                tasks: ["less"]
            }
        }
    });
    
    grunt.loadNpmTasks("grunt-bower-task");
    grunt.loadNpmTasks("grunt-contrib-concat");
    grunt.loadNpmTasks("grunt-contrib-uglify");
    grunt.loadNpmTasks("grunt-contrib-less");
    grunt.loadNpmTasks("grunt-contrib-jshint");
    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.loadNpmTasks("grunt-contrib-clean");

    grunt.registerTask("default", ["jshint", "less", "concat", "uglify"]);
};