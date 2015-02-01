var gulp = require('gulp');
var less = require('gulp-less');
var browserify = require('gulp-browserify');
var LessPluginCleanCSS = require('less-plugin-clean-css');
var cleancss = new LessPluginCleanCSS({advanced: true});

gulp.task('browserify', function() {
    gulp.src('src/main/resources/static/js/tailstreamer.js')
        .pipe(browserify({
            debug: true
        }))
        .pipe(gulp.dest('build/resources/main/static/js'))
});

gulp.task('less', function() {
   gulp.src('src/main/resources/static/less/style.less')
       .pipe(less({
           plugins: [cleancss]
       }))
       .pipe(gulp.dest('build/resources/main/static/css'))
});

gulp.task('default', ['browserify', 'less']);

gulp.task('watch', function() {
    gulp.watch('src/main/resources/static/js/*.js', ['browserify']);
    gulp.watch('src/main/resources/static/less/*.less', ['less']);
});