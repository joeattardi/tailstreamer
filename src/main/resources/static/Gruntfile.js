module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON("package.json"),
        bower: {
            install: { }
        },
        concat: {
            js: {
                src: ["lib/**/*.js", "js/*.js"],
                dest: "dist/<%= pkg.name %>.js"
            }
        },
        uglify: {
            dist: {
                files: {
                    "dist/<%= pkg.name %>.js": ["<%= concat.js.dest %>"]
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
                    "css/style.css": "less/style.less"
                }
            }
        },
        jshint: {
            files: ["Gruntfile.js", "js/tailstreamer.js"]            
        },
        clean: [
            "css/style.css", "dist"
        ],
        watch: {
            less: {
                files: ["less/style.less"],
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

    grunt.registerTask("default", ["jshint", "concat", "uglify"]);
};