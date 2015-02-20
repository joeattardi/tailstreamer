var gulp = require('gulp');
var less = require('gulp-less');
var browserify = require('browserify');
var LessPluginCleanCSS = require('less-plugin-clean-css');
var cleancss = new LessPluginCleanCSS({advanced: true});
var source = require('vinyl-source-stream');
var uglify = require('gulp-uglify');
var streamify = require('gulp-streamify');
var gutil = require('gulp-util');
var postcss = require('gulp-postcss');
var autoprefixer = require('autoprefixer-core');

if (gutil.env.devel) {
    gutil.log('Building in development mode');
}

gulp.task('browserify', function() {
    browserify({
        entries: ['./src/main/resources/static/js/tailstreamer.js'],
        debug: true
    })
        .bundle()
        .pipe(source('tailstreamer.js'))
        .pipe(gutil.env.devel ? gutil.noop() : streamify(uglify()))
        .pipe(gulp.dest('build/resources/main/static/js'));
});

gulp.task('less', function() {
   gulp.src('src/main/resources/static/less/style.less')
       .pipe(less({
           plugins: [cleancss]
       }))
       .pipe(postcss([autoprefixer({browsers: ['last 2 versions']})]))
       .pipe(gulp.dest('build/resources/main/static/css'))
});

gulp.task('default', ['browserify', 'less']);

gulp.task('watch', function() {
    gulp.watch('src/main/resources/static/js/*.js', ['browserify']);
    gulp.watch('src/main/resources/static/less/*.less', ['less']);
});