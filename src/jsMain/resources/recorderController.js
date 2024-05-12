//webkitURL is deprecated but nevertheless
URL = window.URL || window.webkitURL;

let gumStream; 						//stream from getUserMedia()
let rec; 							//Recorder.js object
let input; 							//MediaStreamAudioSourceNode we'll be recording
let url;

// shim for AudioContext when it's not avb. 
const AudioContext = window.AudioContext || window.webkitAudioContext;
let audioContext; //audio context to help us record

document.addEventListener("DOMContentLoaded", function(event) {
	const recordButton = document.getElementById("record-btn");
	const stopButton = document.getElementById("stop-btn");
	const pauseButton = document.getElementById("pause-btn");
	const barElement = document.getElementById("process-bar-2")

	//add events to those 2 buttons
	recordButton.addEventListener("click", startRecording);
	stopButton.addEventListener("click", stopRecording);
	pauseButton.addEventListener("click", pauseRecording);

	function startRecording() {
		barElement.hidden = false
		barElement.innerHTML = "<p class='text-warning' style='float:left;'><b>Recording...</b></p><img src='static/Pulse.gif' width='50px'>"

		// console.log("recordButton clicked");

		/*
			Simple constraints object, for more advanced audio features see
			https://addpipe.com/blog/audio-constraints-getusermedia/
		*/

		const constraints = {audio: true, video: false};

		/*
		   Disable the record button until we get a success or fail from getUserMedia()
	   */

		recordButton.disabled = true;
		stopButton.disabled = false;
		pauseButton.disabled = false

		/*
			We're using the standard promise based getUserMedia()
			https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia
		*/

		navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
			// console.log("getUserMedia() success, stream created, initializing Recorder.js ...");

			/*
				create an audio context after getUserMedia is called
				sampleRate might change after getUserMedia is called, like it does on macOS when recording through AirPods
				the sampleRate defaults to the one set in your OS for your playback device

			*/
			audioContext = new AudioContext();

			//update the format
			// document.getElementById("formats").innerHTML="Format: 1 channel pcm @ "+audioContext.sampleRate/1000+"kHz"

			/*  assign to gumStream for later use  */
			gumStream = stream;

			/* use the stream */
			input = audioContext.createMediaStreamSource(stream);

			/*
				Create the Recorder object and configure to record mono sound (1 channel)
				Recording 2 channels  will double the file size
			*/
			rec = new Recorder(input,{numChannels:1})

			//start the recording process
			rec.record()

			// console.log("Recording started");

		}).catch(function(err) {
			//enable the record button if getUserMedia() fails
			recordButton.disabled = false;
			stopButton.disabled = true;
			pauseButton.disabled = true
		});
	}

	function pauseRecording(){
		console.log("pauseButton clicked rec.recording=",rec.recording );
		if (rec.recording){
			//pause
			rec.stop();
			pauseButton.innerHTML="Resume";
		}else{
			//resume
			rec.record()
			pauseButton.innerHTML="Pause";

		}
	}

	function stopRecording() {
		// console.log("stopButton clicked");

		barElement.innerHTML = "<img src='static/done.png' width='40px' style='float:left'><p class='text-success'><b>Recording done, click to convert.</b></p>"

		//disable the stop button, enable the record too allow for new recordings
		stopButton.disabled = true;
		recordButton.disabled = false;
		pauseButton.disabled = true;

		//reset button just in case the recording is stopped while paused
		pauseButton.innerHTML="Pause";

		//tell the recorder to stop the recording
		rec.stop();

		//stop microphone access
		gumStream.getAudioTracks()[0].stop();

		//create the wav blob and pass it on to createDownloadLink
		rec.exportWAV(createDownloadLink);
	}

	function createDownloadLink(blob) {

		url = URL.createObjectURL(blob);

		//name of .wav file to use during upload and download (without extendion)
		const filename = new Date().toISOString()+Math.floor(Math.random()*10000);

		blobToDataURI(blob, function (base64){
			recordingsList.innerHTML = base64
		})
	}

	function blobToDataURI(blob, callback) {
		var reader = new FileReader();
		reader.readAsDataURL(blob);
		reader.onload = function (e) {
			callback(e.target.result);
		}
	}
});
