#Reddit r/place

https://www.reddit.com/r/place/comments/6396u5/rplace_archive_update/

The binary file __reddit-placec-diffs.bin__ describes the state changes of /r/place.
It is a list of little-endian 4 byte unsigned integers.

A state change is described by 4 integers:

* Timestamp in seconds
* X
* Y
* Color id (see table)

In such way, 16 bytes represent a single state change.

You can reconstruct all snapshots from this data.

##Color table

&nbsp;&nbsp;&nbsp;          Code  &nbsp;&nbsp;&nbsp; Color

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  0 &nbsp;&nbsp;&nbsp; #FFFFFF  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  1 &nbsp;&nbsp;&nbsp; #E4E4E4  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  2 &nbsp;&nbsp;&nbsp; #888888  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  3 &nbsp;&nbsp;&nbsp; #222222  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  4 &nbsp;&nbsp;&nbsp; #FFA7D1  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  5 &nbsp;&nbsp;&nbsp; #E50000  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  6 &nbsp;&nbsp;&nbsp; #E59500  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  7 &nbsp;&nbsp;&nbsp; #A06A42  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  8 &nbsp;&nbsp;&nbsp; #E5D900  <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  9 &nbsp;&nbsp;&nbsp; #94E044  <br/>
&nbsp;&nbsp;&nbsp;             10 &nbsp;&nbsp;&nbsp; #02BE01  <br/>
&nbsp;&nbsp;&nbsp;             11 &nbsp;&nbsp;&nbsp; #00E5F0  <br/>
&nbsp;&nbsp;&nbsp;             12 &nbsp;&nbsp;&nbsp; #0083C7  <br/>
&nbsp;&nbsp;&nbsp;             13 &nbsp;&nbsp;&nbsp; #0000EA  <br/>
&nbsp;&nbsp;&nbsp;             14 &nbsp;&nbsp;&nbsp; #E04AFF  <br/>
&nbsp;&nbsp;&nbsp;             15 &nbsp;&nbsp;&nbsp; #820080  <br/>

##Maximum events per ByteBuffer

CubesVertexBufferObjectPackedBuffers uses ByteBuffer.allocateDirect to allocate its internal buffer.
This means the buffer can be no larger than 2^31-1 or 2.147.483.647 bytes.

Given that for any VBO the number of bytes is equal to product of
- the sum number of floats per color and the number of floats per position
- the number of elements per cube
- the number of cubes
- the number of bytes per float

in code:

(FLOATS_PER_COLOR + FLOATS_PER_POSITION) * ELEMENTS_PER_CUBE * cubes.size() * BYTES_PER_FLOAT

Thus finding the maximum numbers of cubes per VBO by solving

<center>(FLOATS_PER_COLOR + FLOATS_PER_POSITION) * ELEMENTS_PER_CUBE * cubes.size() * BYTES_PER_FLOAT =< 2.147.483.647</center>


 - FLOATS_PER_COLOR is equal to 4
 - FLOATS_PER_POSITION is equal to 3
 - ELEMENTS_PER_CUBE is equal to ELEMENTS_PER_FACE * FACES_PER_CUBE
    - ELEMENTS_PER_FACE is equal to 6
    - FACES_PER_CUBE is equal to 3
    - so ELEMENTS_PER_CUBE is equal to 18
 - BYTES_PER_FLOAT is equal to 4


<center>(FLOATS_PER_COLOR + FLOATS_PER_POSITION) * ELEMENTS_PER_CUBE * cubes.size() * BYTES_PER_FLOAT =< 2.147.483.647</center>
<center>↓</center>
<center>(4 + 3) * 18 * cubes.size() * 4 =< 2.147.483.647</center>
<center>↓</center>
<center>7 * 18 * cubes.size() * 4 =< 2.147.483.647</center>
<center>↓</center>
<center>7 * 18 * cubes.size() * 4 =< 2.147.483.647</center>
<center>↓</center>
<center>504 * cubes.size() =< 2.147.483.647</center>
<center>↓</center>
<center>cubes.size() =< ~4260880</center>


## Official Update

https://redditblog.com/2017/04/18/place-part-two/
https://www.reddit.com/r/redditdata/comments/6640ru/place_datasets_april_fools_2017/

Downloaded the full dataset as a gzipped CSV.

The headers of the CSV file are __ts, user_hash, x_coordinate, y_coordinate, color__
the user hash can be calculated with <code>TO_BASE64(SHA1(__%username%__))</code>

The data file __diffs.bin__ now contains the binary data of the full dataset.

The total number of events is equal to __16.559.897__