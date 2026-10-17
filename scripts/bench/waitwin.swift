// Poll the window server until some on-screen window's title contains a needle,
// then print the epoch milliseconds of that observation.
//
// CGWindowListCopyWindowInfo is used rather than the Accessibility API because it
// needs no per-app AX round trip: one call returns every on-screen window, so a
// poll costs well under a millisecond and the measurement granularity is the
// sleep interval, not the query.
import Foundation
import CoreGraphics

let args = CommandLine.arguments
guard args.count >= 3 else {
    FileHandle.standardError.write("usage: waitwin NEEDLE TIMEOUT_MS [OWNER]\n".data(using: .utf8)!)
    exit(2)
}
let needle = args[1].lowercased()
let timeoutMs = Double(args[2]) ?? 90_000
let owner = args.count >= 4 ? args[3].lowercased() : nil

func nowMs() -> Double { Date().timeIntervalSince1970 * 1000 }

let deadline = nowMs() + timeoutMs

while nowMs() < deadline {
    if let list = CGWindowListCopyWindowInfo([.optionOnScreenOnly, .excludeDesktopElements], kCGNullWindowID) as? [[String: Any]] {
        for w in list {
            guard let name = w[kCGWindowName as String] as? String, !name.isEmpty else { continue }
            if let owner, let o = w[kCGWindowOwnerName as String] as? String,
               !o.lowercased().contains(owner) { continue }
            if name.lowercased().contains(needle) {
                print(Int(nowMs()))
                exit(0)
            }
        }
    }
    usleep(5_000)   // 5 ms
}
exit(1)
