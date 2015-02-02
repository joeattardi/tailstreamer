var gulp = require('gulp');
var less = require('gulp-less');
var browserify = require('browserify');
var LessPluginCleanCSS = require('less-plugin-clean-css');
var cleancss = new LessPluginCleanCSS({advanced: true});
var source = require('vinyl-source-stream');

gulp.task('browserify', function() {
    browserify({
        entries: ['./src/main/resources/static/js/tailstreamer.js'],
        debug: true
    })
        .bundle()
        .pipe(source('tailstreamer.js'))
        .pipe(gulp.dest('build/resources/main/static/js'));
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