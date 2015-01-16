package nivance.iso.file;


import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FileWatchService {

	private final WatchService watcher;
	private final boolean recursive;
	private Kind<?>[] events;
	private boolean trace = false;
	private int count;
	
	/**
	 * Creates a WatchService and registers the given directory
	 */
	FileWatchService(Path dir, boolean recursive, WatchEvent.Kind<?>... events) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.recursive = recursive;
		this.events = events;
		if (recursive) {
			log.debug("Recurse watching {} ...", dir);
			registerAll(dir);
		} else {
			log.debug("Watching {} ...", dir);
			register(dir);
		}
		// enable trace after initial registration
		this.trace = true;
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir) throws IOException {
		dir.register(watcher, events);
		count++;
		if (trace)
			log.debug("register: {}", dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	protected void processEvents() {
		for (;;) {
			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}
				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path name = ev.context();
				Path child = ((Path) key.watchable()).resolve(name);
				// print out event
				log.debug("{}: {}", kind.name(), child);
				fireFileEvent(kind, child);
				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				if (recursive && (kind == ENTRY_CREATE)) {
					try {
						if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
							registerAll(child);
						}
					} catch (IOException x) {
						// ignore to keep sample readbale
						log.warn("dir: {} failed to watch", child, x);
					}
				}
			}
			// reset key
			boolean valid = key.reset();
			if (!valid) {
				// directory no longer accessible
				count--;
				if (count == 0)
					break;
			}
		}
	}

	protected abstract void fireFileEvent(Kind<?> kind, Path child);

}
